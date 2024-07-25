package gift.service;

import gift.constants.ErrorMessage;
import gift.dto.OptionSubtractRequest;
import gift.dto.OrderRequest;
import gift.entity.Member;
import gift.entity.Product;
import gift.repository.MemberJpaDao;
import gift.repository.ProductJpaDao;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductJpaDao productJpaDao;
    private final MemberJpaDao memberJpaDao;

    private final OptionService optionService;
    private final MemberService memberService;

    public OrderService(ProductJpaDao productJpaDao, MemberJpaDao memberJpaDao,
        OptionService optionService, MemberService memberService) {
        this.productJpaDao = productJpaDao;
        this.memberJpaDao = memberJpaDao;
        this.optionService = optionService;
        this.memberService = memberService;
    }

    @Transactional
    public void order(OrderRequest orderRequest, String email) {
        Member member = memberJpaDao.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.MEMBER_NOT_EXISTS_MSG));

        Product product = productJpaDao.findById(orderRequest.getProductId())
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PRODUCT_NOT_EXISTS_MSG));

        OptionSubtractRequest optionSubtractRequest = new OptionSubtractRequest(orderRequest);
        optionService.subtractOption(optionSubtractRequest);
        memberService.deleteWishlist(email, product.getId());

    }

}
